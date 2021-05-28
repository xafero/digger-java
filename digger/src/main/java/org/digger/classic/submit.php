<?php

/**
 * Thanks to Scorcerer (Scorcerer@gmx.net) for this PHP port of the score saving code.
 * To use, your html source should look something like this:
 * <APPLET CODE=Digger.class archive=digger.jar WIDTH=320 HEIGHT=200 hspace=0 vspace=0 border=0>
 *    <param name="speed" value=66>
 *    <param name="submit" value="www.digger.org/submit.php">
 * </APPLET>
 *
 * Note that the "submit" param is the full URL of this script, minus the "http://".
 * The following changes need to be made to Scores.java:
 * _submit method:
 *   substr = "?player=" + n + "&score=" + s + "&check1=" + ms + "&check2=" +
 *            ( (ms+32768) * s) % 65536;
 * run method:
 *   URL u = new URL ("http://" + dig.subaddr + (substr==null?"":substr));
 *
 * The $file1 is the path of the scores files. Note that you're able to put the scores-file
 * outside of the www-folder and no-one except the script will be able to make changes to the
 * file.
 */

$dir=".";
$file1 = "../../scores.dat";
$insert = 1;
$MAX_LENGTH = 3;

// disable scores if no player present
if (strlen($player) == 0) $insert = 0;

if ($insert > 0)
	if ( ((($check1 + 32768) * $score) % 65536 != $check2) || ($check1 <= 15) )
	{
		// illegal update

		echo "<H3>\n<BR>Je hebt geprobeert een clandestiene score toe te voegen.\n<BR>";
		echo "Hierbij is je ip-adres ($REMOTE_ADDR) gelogd.\n";
		echo "Bij herhaaldelijke overtreding zullen er sancties in werking treden.\n<BR>\n</H3>";
		exit;
	}


$update = $insert;

if (file_exists($file1))
{
	$fp = fopen($file1,"r");

	for ($i=0; $i<10; $i++)
	{
		$rname[$i] = chop(fgets($fp, 260));
		$rscore[$i] = chop(fgets($fp, 260));

		if ((strlen($rname[$i]) == 0) || (strlen($rscore[$i]) == 0))
		{
			// insert default score
			$rname[$i] = "...";
			$rscore[$i] = "0";
			$update=-1;
		}

		if ($insert > 0)
			if ($rscore[$i] < $score)
			{
				// move other score down
				if (i < 10)
				{
					$rname[$i+1] = $rname[$i];
					$rscore[$i+1] = $rscore[$i];
				}
				$rname[$i] = $player;
				$rscore[$i] = $score;
				$i++;
				$insert--;
			}
	}
	fclose($fp);

	$result="";
	for ($i=0; $i<10; $i++)
	{
		// make sure the players name has a length of $MAX_LENGTH
		$rname[$i] = substr($rname[$i]."     ", 0, $MAX_LENGTH);

		$result = $result."$rname[$i]\n$rscore[$i]\n";
	}

	if ($insert != $update)
	{
		$openFILE = fopen($file1, "w");
		fwrite($openFILE, $result);
		fclose($openFILE);
	}

	echo $result;
}

?>