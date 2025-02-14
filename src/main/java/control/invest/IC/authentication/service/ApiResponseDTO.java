package control.invest.IC.authentication.service;


import org.springframework.http.ResponseEntity;

public class ApiResponseDTO {
    private Object status_res;
    private int status_code;
    private String status_msg;



    public ApiResponseDTO(Object status_res, String status_msg, int status_code) {
        this.status_res = status_res;
        this.status_msg = status_msg;
        this.status_code = status_code;
    }

    public Object getStatus_res() {
        return status_res;
    }

    public void setStatus_res(Object status_res) {
        this.status_res = status_res;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }
}
