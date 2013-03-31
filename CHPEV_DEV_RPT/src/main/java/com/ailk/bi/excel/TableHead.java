package com.ailk.bi.excel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于Excel多表头的表格表头对象，含有一个表头行对象列表
 * 每个表头定义一定要单独指定最后一行，因为最后一行所有单元格没有跨列了，单元格可以用来定义下面数据行的一些处理,如省份显示名称
 */
public class TableHead implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1319362476830614977L;
	/**
     * 表头行对象列表
     */
    private List<TableHeadTr> head = null;

    /**
     * 获取表头行对象列表
     * 
     * @return
     */
    public List<TableHeadTr> getHead() {
        return head;
    }

    /**
     * 设置表头行对象列表
     * 
     * @param head
     */
    public void setHead(List<TableHeadTr> head) {
        this.head = head;
    }

    /**
     * 向列表中增加一个表头行对象
     * 
     * @param ttr
     */
    public void addTableHeadRow(TableHeadTr thtr) {
        if (null == head) {
            head = new ArrayList<TableHeadTr>();
        }
        head.add(thtr);
    }
}
