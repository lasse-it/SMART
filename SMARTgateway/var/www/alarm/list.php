<?php
$output = null;
foreach (glob(__DIR__.'/alarms/*') as $file) {
    $file = realpath($file);
    $basename = basename($file);
    $time = file_get_contents('alarms/'.$basename);
    $output = $output.substr($time, 0, -1).", ";
}
$output = substr($output, 0, -2);
$output = '{ "alarms":['.$output;
$output = $output."]}";
echo $output;
?>