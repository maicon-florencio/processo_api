package br.com.dock.processo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProcessoDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idProcess;
    @NotNull
    private int logic;
    private int serial;
    private String model;
    private int sam;
    @NotEmpty
    private String ptid;
    private int plat;
    @NotEmpty
    private String version;
    private int mxr;
    private int mxf;
    @NotEmpty
    private String pverfm;

}
