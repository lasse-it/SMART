<?php
$alarm = $_GET["alarm"];
unlink('alarms/'.$alarm);
$url = '127.0.0.1/alarm/reload.php';
$ch = curl_init($url);
curl_exec($ch);
curl_close($ch);
header("Location: index.php");
die();
?>