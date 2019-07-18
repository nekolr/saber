package com.nekolr.saber.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageVO implements Serializable {

    private Object data;

    private Long totalSize;
}
