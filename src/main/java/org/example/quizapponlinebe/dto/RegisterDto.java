package org.example.quizapponlinebe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto implements Validator {
    private String username;
    private String password;
    private String passwordConfirm;
    private String name;
    private String email;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterDto registerDto = (RegisterDto) target;
        if(registerDto.getUsername()==null||registerDto.getUsername().trim().isEmpty()){
            errors.rejectValue("username",null,"Tên đăng nhập không được để trống!");
        } else if(!registerDto.getUsername().matches("^[a-zA-Z0-9]{5,20}$")){
            errors.rejectValue("username",null,"Tên đăng nhập gồm chữ cái và số, có độ dài 5-20 ký tự!");
        }
        if(registerDto.getPassword()==null||registerDto.getPassword().trim().isEmpty()){
            errors.rejectValue("password",null,"Mật khẩu không được để trống!");
        } else if(!registerDto.getPassword().matches("^.{8,}$")){
            errors.rejectValue("password",null,"Mật khẩu ít nhất 8 ký tự!");
        } else if(!registerDto.getPassword().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).+$")){
            errors.rejectValue("password",null,"Mật khẩu phải có chữ in hoa, chữ thường, số và ký tự đặc biệt!");
        }
        if(!registerDto.getPassword().equals(registerDto.getPasswordConfirm())){
            errors.rejectValue("passwordConfirm",null,"Mật khẩu không khớp!");
        }
        if(registerDto.getName()==null||registerDto.getName().trim().isEmpty()){
            errors.rejectValue("name",null,"Tên không được để trống!");
        } else if(!registerDto.getName().matches("^(?=(?:.*[A-Za-z]){3,})([A-Z][a-z]*)(?:\\s+[A-Z][a-z]*)*$")){
            errors.rejectValue("name",null,"Tên ít nhất 3 ký tự, viết hoa ký tự đầu tiên mỗi chữ!");
        }
        if(registerDto.getEmail()==null||registerDto.getEmail().trim().isEmpty()){
            errors.rejectValue("email",null,"Email không được để trống!");
        } else if(!registerDto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            errors.rejectValue("email",null,"Email không đúng định dạng!");
        }
    }
}
