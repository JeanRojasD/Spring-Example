package com.br.example.dto.form;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderForm {

    private String name;
    private String cnpj;

}
