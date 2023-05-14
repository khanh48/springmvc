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
import me.forum.Controller.BaseController;
import me.forum.Entity.User;
import me.forum.WebSocketSetup.UserHandler;

public class ChatBot {
	//bQ1qIbHMvw94j8NmkjTmT3BlbkFJHAEMc7eqcqvcDDIUW1bm
	final static String token = "GxOI3NCNi1MUzx6OrIpnT3BlbkFJm9SqYc50ThV8sb1jdETS";
	OpenAiService service;
	final List<ChatMessage> messages;
	User user, chatBot;

	boolean isStoped;

	public ChatBot(User user) {
		this.user = user;
		chatBot = BaseController.GetInstance().userDao.findUserByUserName("chatbot");
		service = new OpenAiService(token, Duration.ofSeconds(30));

		messages = new ArrayList<>();
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
				"Bạn là trợ lý thông minh của website diễn đàn phượt. Hãy trả lời ngắn gọn, đầy đủ nhất có thể."));
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
				"Bạn đang trò chuyện với người dùng tên: '" + user.getHoten() + "'."));
		messages.add(new ChatMessage(ChatMessageRole.SYSTEM.value(),
				"Hãy chào người dùng và giới thiệu bản thân khi bắt đầu trò chuyện."));

		isStoped = false;
		System.out.println(user.getTaikhoan() + " started");
	}

	public void request(String msg) {
		ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), msg);
		messages.add(userMessage);
		ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model("gpt-3.5-turbo")
				.messages(messages).n(1).maxTokens(1500).logitBias(new HashMap<>()).build();
		service.streamChatCompletion(chatCompletionRequest).doOnError(new Consumer<Throwable>() {

			@Override
			public void accept(Throwable t) throws Exception {
				System.out.println("Oops, Error: "+ t.getMessage());
				stop();
			}
		}).blockingForEach(new Response(user.getTaikhoan()));
	}

	public void stop() {
		service.shutdownExecutor();
		isStoped = true;
		System.out.println(user.getTaikhoan() + " stoped");
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
			if (isStoped)
				return;
			ChatCompletionChoice choice = t.getChoices().get(0);
			JSONObject json = new JSONObject();
			json.put("isStop", false);
			if ("stop".equals(choice.getFinishReason()) || "lenght".equals(choice.getFinishReason())) {
				response = response.replaceAll("^null", "");
				messages.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), response));
				BaseController.GetInstance().messageDao.AddMessage(chatBot.getTaikhoan(), user, response);
				BaseController.GetInstance().messageDao.makeAsRead(chatBot.getTaikhoan(), user);
				response = "";
				json.put("isStop", true);
			}
			json.put("type", "newResult");
			json.put("value", choice.getMessage().getContent());
			json.put("linkAvatar", chatBot.getAnhdaidien());

			UserHandler.GetInstance().send(user, json.toString());
			response += choice.getMessage().getContent();

		}

	}
}
