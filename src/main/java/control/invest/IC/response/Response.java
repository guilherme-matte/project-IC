package control.invest.IC.response;

import control.invest.IC.authentication.service.ApiResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class Response {

    public ResponseEntity<ApiResponseDTO> response(Object obj, String message, int code) {
        ApiResponseDTO response = new ApiResponseDTO(obj, message, code);
        return ResponseEntity.status(code).body(response);
    }
}
