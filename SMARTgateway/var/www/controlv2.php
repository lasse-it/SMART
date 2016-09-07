<?php
include 'config.php';
$cmd = $_GET["cmd"];
$ip = $_GET["ip"];
$token = $_GET["token"];

if ($token == $key) {
echo file_get_contents("http://" . $ip . "/controlv2.php?cmd=" . $cmd);
}
?>