package likelion.springbootBaco.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class MemberDto {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Create {
        private String name;
        private String city;
        private String state;
        private String street;
        private String zipcode;
    }
}
