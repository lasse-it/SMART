<?php
$id = $_GET["id"];
$json = json_decode(file_get_contents("https://api.spotify.com/v1/tracks/".$id));
echo json_encode($json);
?>