<?php
#Atfirst create the connection string
$connect=mysqli_connect("localhost","root","");
mysqli_select_db($connect,"studentguidance");
# querey
$qry="SELECT * from `facultyinfo`";
$result=mysqli_query($connect,$qry);
while($res=mysqli_fetch_array($result)){
    $img=array("image"=>"http:/192.168.29.116/StudentGuidance/IMAGES/".$res["image"]);
    $change=array_replace($res,$img);
    $data[]=$change;
}
echo json_encode($data);
?>