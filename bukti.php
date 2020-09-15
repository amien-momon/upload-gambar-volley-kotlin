<?php

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "travel";

$conn = mysqli_connect($servername, $username, $password, $dbname);


$respons = array();

    $image = base64_decode($_POST['foto']);
    
    $namaimage =  rand(1,100);
    $tanggal = date("d-m-y");
 
    $nama = "Bukti-".$tanggal."-".$namaimage;
    $target_dir = "Foto/".$nama.".jpeg";
    $ServerURL = "$target_dir";
    
    //inser mysql
    $insert = "INSERT INTO bukti (id,gambar,dir) values(NULL,'$nama','$ServerURL')";

    if(mysqli_query($conn, $insert)){
        if (file_put_contents($target_dir, $image)) {
            $respons['error'] =false;
            $respons['massage'] = "Upload berhasil";
        }else{
            $respons['error'] =true;
            $respons['massage'] ="salah";
        }

    echo json_encode($respons);
    mysqli_close($conn);
    }else{

        $respons['error'] =true;
        $respons['massage'] ="ulangi";

    }


 ?>
