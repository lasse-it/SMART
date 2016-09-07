<?php
$year = $_GET["year"];
$month = $_GET["month"];
$day = $_GET["day"];
$hour = $_GET["hour"];
$minute = $_GET["minute"];
$light = $_GET["light"];
$sound = $_GET["sound"];
$spotifyurl = $_GET["spotifyurl"];
$alarmlights = $_GET["alarmlights"];
$soundgpios =  $_GET["soundgpio"];

switch ($month) {
	case 1:
		$monthastext = "January";
	break;
	case 2:
		$monthastext = "February";
	break;
	case 3:
		$monthastext = "March";
	break;
	case 4:
		$monthastext = "April";
	break;
	case 5:
		$monthastext = "May";
	break;
	case 6:
		$monthastext = "June";
	break;
	case 7:
		$monthastext = "July";
	break;
	case 8:
		$monthastext = "August";
	break;
	case 9:
		$monthastext = "September";
	break;
	case 10:
		$monthastext = "October";
	break;
	case 11:
		$monthastext = "November";
	break;
	case 12:
		$monthastext = "December";
	break;
		
}
$hourastext = $hour;
$minuteastext = $minute;
for ($i=0; $i<10; $i++) {
	if ($hour == $i) {
		$hourastext = "0" . $hour;
	}
	if ($minute == $i) {
		$minuteastext = "0" . $minute;
	}
}


$alarmlight = "none";
if ($alarmlights != "none" && $alarmlights != "") {
	$alarmlight = '"' . $alarmlights . '"';
}

$soundgpio = "none";
if ($soundgpios != "none" && $soundgpios != "") {
	$soundgpio = '"' . $soundgpios . '"';
}

$cronfile = $year.$month.$day.$hour.$minute;
$cronreadable = $hourastext.':'.$minuteastext.' '.$day.'. '.$monthastext.' '.$year;
$cron = $minute.' '.$hour.' '.$day.' '.$month." * sudo bash /root/smart/v2/alarm ".$year.' "'.$spotifyurl.'" '.$light.' '.$sound." ".$alarmlight." ".$soundgpio;
$arr = array(
		'text' => $cronreadable,
		'cron' => $cron,
		'sound' => $sound,
		'light' => $light,
		'spotifyurl' => $spotifyurl,
		'filename' => $cronfile,
		'alarmlights' => $alarmlight,
		'soundgpio' => $soundgpio,
		'year' => $year,
		'month' => $month,
		'monthastext' => $monthastext,
		'day' => $day,
		'hour' => $hour,
		'hourastext' => $hourastext,
		'minute' => $minute,
		'minuteastext' => $minuteastext,
		'cmd' => $alarmlight
		
);
$fileinput = json_encode($arr);
file_put_contents('alarms/'.$cronfile,$fileinput.PHP_EOL);
$url = '127.0.0.1/alarm/reload.php';
$ch = curl_init($url); 
curl_exec($ch);
curl_close($ch);
header("Location: index.php");
die();
?>