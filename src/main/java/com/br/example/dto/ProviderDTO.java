package com.br.example.dto;

import com.br.example.model.Provider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDTO {

    private Long id;
    private String name;
    private String cnpj;

    public static ProviderDTO from(Provider provider){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(provider, ProviderDTO.class);
    }

}
