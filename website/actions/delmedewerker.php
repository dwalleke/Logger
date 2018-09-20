<?php
session_start();
include("../db.php");
deleteMedewerker($db, $_SESSION["bid"], $_POST["mid"]);
echo "ok";
?>