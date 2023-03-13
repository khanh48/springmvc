<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="left">
    <div class="info">
        <a href="./profile.php?user=<?php echo $my_id ?>">
            <div class="info-top">Hồ sơ cá nhân</div>
        </a>
        <div class="thongbao">
            <a href="./notification">
                <div class="tb">Thông báo</div>
            </a>
            <div class="notify">
                <?php
                $getNotify = $con->query("SELECT * FROM thongbao WHERE nguoinhan = '$my_id' ORDER BY ngaytao DESC");
                if ($getNotify->num_rows > 0) {
                    while ($notify = $getNotify->fetch_assoc()) {
                        echo "<a href='" . $notify["url"] . "&r=" . $notify["mathongbao"] . "'><div class='notify-content " . ($notify["trangthai"] ? "" : "unread") . "'>
                        " . $notify["noidung"] . "
                        </div></a>";
                    }
                }
                ?>
            </div>
        </div>
    </div>
    <div class="group">
        <div class="name group-name">Bắc</div>
        <div class="ps-1">
            <?php
            $sql = "SELECT * FROM baiviet WHERE nhom = 'Bắc' ORDER BY mabaiviet DESC LIMIT 0,3";
            $result = $con->query($sql);
            if ($result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
                    echo "<div><a href='./post.php?id=" . $row['mabaiviet'] . "'>" . $row['tieude'] . "</a>
                                </div>";
                }
            }
            ?>
        </div>
    </div>
    <div class="group">
        <div class="name group-name">Trung</div>
        <div class="ps-1">
            <?php
            $sql = "SELECT * FROM baiviet WHERE nhom = 'Trung' ORDER BY mabaiviet DESC LIMIT 0,3";
            $result = $con->query($sql);
            if ($result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
                    echo "<div><a href='./post.php?id=" . $row['mabaiviet'] . "'>" . $row['tieude'] . "</a>
                                </div>";
                }
            }
            ?>
        </div>
    </div>
    <div class="group">
        <div class="name group-name">Nam</div>
        <div class="ps-1">
            <?php
            $sql = "SELECT * FROM baiviet WHERE nhom = 'Nam' ORDER BY mabaiviet DESC LIMIT 0,3";
            $result = $con->query($sql);
            if ($result->num_rows > 0) {
                while ($row = $result->fetch_assoc()) {
                    echo "<div><a href='./post.php?id=" . $row['mabaiviet'] . "'>" . $row['tieude'] . "</a>
                                </div>";
                }
            }
            ?>
        </div>
    </div>
</div>