<?php
class DB_CONNECT{
    private $conn;
    public function connect() {
        require_once 'dbConfig.php';
        $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
        return $this->conn;
        
    }
}
?>

