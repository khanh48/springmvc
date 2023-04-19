package me.forum.Module;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import io.reactivex.functions.Consumer;
import me.forum.Entity.User;
import me.forum.WebSocketSetup.UserHandler;

public class ChatBot {
	final static String token = "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";
	OpenAiService service;
	final List<ChatMessage> messages;
	User user;

	boolean isStoped;

	public ChatBot(User user) {
		this.user = user;
		service = new OpenAiService(token, Duration.ofSeconds(30));
		
		messages = new ArrayList<>();
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
				"Tôi là trợ lý thông minh của website diễn đàn phượt."));
		messages.add(
				new ChatMessage(ChatMessageRole.SYSTEM.value(), "tôi đang trò chuyện với người dùng tên: '" + user.getHoten() + "'."));
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
				"Hãy chào người dùng và giới thiệu bản thân khi bắt đầu trò chuyện."));

		isStoped = false;
		System.out.println("start");
	}

	public void request(String msg) {
		ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), msg);
		messages.add(userMessage);
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model("gpt-3.5-turbo")
				.messages(messages).n(1).maxTokens(1500).logitBias(new HashMap<>()).build();
		service.streamChatCompletion(chatCompletionRequest).doOnError(Throwable::printStackTrace)
				.blockingForEach(new Response(user.getTaikhoan()));
		System.out.println(messages.size());
	}

	public void stop() {
		service.shutdownExecutor();
		isStoped = true;
		System.out.println("stop");
	}

	public boolean isStoped() {
		return isStoped;
	}

	class Response implements Consumer<ChatCompletionChunk> {
		String user;
		String response;

		public Response(String user) {
			this.user = user;
			response = "";
		}

		@Override
		public void accept(ChatCompletionChunk t) throws Exception {
			ChatCompletionChoice choice = t.getChoices().get(0);
			if ("stop".equals(choice.getFinishReason()) || "lenght".equals(choice.getFinishReason())) {
				messages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), response));
				response = "";
			}
			JSONObject json = new JSONObject();
			json.put("type", "newResult");
			json.put("value", choice.getMessage().getContent());
			UserHandler.GetInstance().send(user, json.toString());
			response += choice.getMessage().getContent();

		}

	}
}
