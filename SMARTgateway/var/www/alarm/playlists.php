<?php
$data = array("method"=> "core.playlists.get_playlists", "jsonrpc"=> "2.0", "params"=> array("include_tracks"=> null), "id"=> 1);
$data_string = json_encode($data);
$ch = curl_init('http://10.0.1.106:6680/mopidy/rpc');
curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
curl_setopt($ch, CURLOPT_POSTFIELDS, $data_string);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, array(
		'Content-Type: application/json',
		'Content-Length: ' . strlen($data_string))
);

$result = curl_exec($ch);
echo $result;
?>