package org.pet.home.net.param;

import lombok.Data;

/**
 * @description:TODO
 * @author: 龚强
 * @data:
 **/
@Data
public class Departmentparam {
    private String sn;
    private String name;
    private String dirPath;
    private  int state;

    private long parentId;
}
