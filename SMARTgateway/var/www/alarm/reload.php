<?php
#add existing alarms to temp
$url = '127.0.0.1/alarm/list.php';
$ch = curl_init($url);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$json = curl_exec($ch);
curl_close($ch);
$obj = json_decode($json,true);
$alarms = $obj['alarms'];
$alarmnum = count($alarms);
$existingalarms = null;
for ($i=0; $i < $alarmnum; $i++){
	$existingalarms = $existingalarms.$alarms[$i]["cron"].PHP_EOL;
}
$existingalarms = $existingalarms."@reboot sudo /usr/local/bin/mopidy --config /etc/mopidy.conf > /var/log/mopidy".PHP_EOL."@reboot sudo /usr/bin/python3 /root/smart/v2/service.py".PHP_EOL;
file_put_contents('alarm.tmp', $existingalarms);

#clean crontab and add alarms
shell_exec("crontab alarm.tmp");
?>