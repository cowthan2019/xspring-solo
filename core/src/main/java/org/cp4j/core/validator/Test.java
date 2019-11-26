package org.cp4j.core.validator;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class Test {


    public static void main(String[] args) {
    }


    public static class AbstractForm {


        @NotNull(message = "AbstractForm - id NotNull")
        private Long id = 1L;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    @Data
    public static class FormEntity extends AbstractForm{

        @NotBlank(message = "FormEntity - message NotBlank")
        @Size(min = 3, max = 5, message = "message长度为3到5")
        private String message = "233";

        @NotBlank(message = "FormEntity - comment NotBlank")
        private String comment = " ";

        @NotEmpty(message = "FormEntity - itemList NotEmpty")
        public List<FormItem> itemList = new ArrayList<>();

        @NotNull(message = "FormEntity - item1 NotNull")
        public FormItem item1;

        public FormItem item2;

        @Data
        public static class FormItem{

            @NotNull(message = "级联才能校验到这里 - FormItem code NotNull")
            private Integer code;

            @NotBlank(message = "级联才能校验到这里 - message code NotBlank")
            private String message;

        }


    }

}
