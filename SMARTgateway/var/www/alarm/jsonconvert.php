<?php
$file = "playlist.tmp";
$json = file_get_contents($file);
$json = json_decode($json, true);
$result = $json['result'];
$tracks = count($result);
for ($i=0; $i<$tracks; $i++){
	$output = $result[$i]["uri"];
	shell_exec("sudo /root/smart/v2/alarm track ".$output);
}
shell_exec("sudo /root/smart/v2/alarm start");
?>