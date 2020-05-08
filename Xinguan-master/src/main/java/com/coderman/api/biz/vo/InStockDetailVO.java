package com.coderman.api.biz.vo;

import com.coderman.api.biz.pojo.Product;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author team
 * @Date 2020/4/20 16:51
 * @Version 1.0
 **/
@Data
public class InStockDetailVO {

    private String inNum;

    private Integer status;

    private Integer type;

    private String operator;

    private SupplierVO supplierVO;

    private List<InStockItemVO> itemVOS=new ArrayList<>();

}


