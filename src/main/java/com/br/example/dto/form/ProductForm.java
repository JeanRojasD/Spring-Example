package com.br.example.dto.form;


import com.br.example.dto.ProviderDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductForm {

    private String name;
    private Integer qtd;
    private Double price;
    private List<ProviderDTO> provider;

}
