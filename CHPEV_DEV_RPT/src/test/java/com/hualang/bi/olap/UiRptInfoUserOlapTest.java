package com.ailk.bi.olap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.PropertyValueException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.ailk.bi.base.BaseTestTemplate;
import com.ailk.bi.domain.olap.UiRptInfoUserOlap;
import com.ailk.bi.domain.olap.UiRptMetaUserOlapDim;
import com.ailk.bi.olap.service.IUiRptInfoUserOlapSrv;

/**
 * 此测试仅可以作为DAO类的测试，由于 SSH的DAO封装的非常简单 因此，也可以作为SERVICE的测试
 *
 * @author d90080502
 *
 */
public class UiRptInfoUserOlapTest extends BaseTestTemplate {
	@Resource
	private IUiRptInfoUserOlapSrv userOlapSrv;
	private static Integer id;
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		System.out.print("测试开始");
	}

	@After
	public void tearDown() throws Exception {
		System.out.print("测试结束");
	}

	@BeforeTransaction
	public void beforeTransaction() {
		System.out.println("事务开始");
	}

	@AfterTransaction
	public void afterTransaction() {
		System.out.println("事务结束");
	}

	@Test
	public void testInsertNull() {
		UiRptInfoUserOlap userOlap = null;
		exception.expect(IllegalArgumentException.class);
		userOlapSrv.save(userOlap);
	}

	@Test
	public void testInsertNotNullWithNull() {
		UiRptInfoUserOlap userOlap = new UiRptInfoUserOlap();
		userOlap.setUserId("admin");
		userOlap.setReportId("1000001");
		exception.expect(PropertyValueException.class);
		userOlapSrv.save(userOlap);
	}

	@Test
	public void testInsert() {
		UiRptInfoUserOlap userOlap = new UiRptInfoUserOlap();
		userOlap.setCustomRptName("这是一个测试");
		userOlap.setUserId("admin");
		userOlap.setReportId("1000001");
		userOlap.setDisplayMode("1");
		userOlap.setIsValid("Y");
		assertEquals(true, userOlapSrv.save(userOlap));
		id = userOlap.getCustomRptId();
	}

	@Test
	public void testFindByIdNull() {
		exception.expect(IllegalArgumentException.class);
		userOlapSrv.findById(null);
	}

	@Test
	public void testFindById() {
		assertNull(userOlapSrv.findById(0));
		UiRptInfoUserOlap userOlap = userOlapSrv.findById(id);
		assertEquals("这是一个测试", userOlap.getCustomRptName());
	}

	@Test
	public void testRemoveById() {
		assertFalse(userOlapSrv.removeById(null));
		assertFalse(userOlapSrv.removeById(0));
		assertEquals(true, userOlapSrv.removeById(id));
	}

	@Test
	public void testRemove() {
		assertFalse(userOlapSrv.remove(null));
		UiRptInfoUserOlap userOlap = new UiRptInfoUserOlap();
		userOlap.setCustomRptName("这是一个测试");
		userOlap.setUserId("admin");
		userOlap.setReportId("1000001");
		userOlap.setDisplayMode("1");
		userOlap.setIsValid("Y");
		userOlapSrv.save(userOlap);
		Integer customRptId = userOlap.getCustomRptId();
		userOlap.setCustomRptId(null);
		assertFalse(userOlapSrv.remove(userOlap));
		userOlap.setCustomRptId(customRptId);
		assertEquals(true, userOlapSrv.remove(userOlap));
	}

	@Test
	public void testSaveReportDimNull() {
		String cusRptId = null;
		List<UiRptMetaUserOlapDim> rptDims = null;
		exception.expect(NullPointerException.class);
		userOlapSrv.saveReportDim(cusRptId, rptDims);
	}

	@Test
	public void testSaveReportDimBlank() {
		String cusRptId = null;
		List<UiRptMetaUserOlapDim> rptDims = null;

		cusRptId = "";
		exception.expect(NullPointerException.class);
		userOlapSrv.saveReportDim(cusRptId, rptDims);
	}

	@Test
	public void testSaveReportDim() {
		String cusRptId = null;
		List<UiRptMetaUserOlapDim> rptDims = null;
		rptDims = new ArrayList<UiRptMetaUserOlapDim>();

		cusRptId = "10006";
		UiRptMetaUserOlapDim userOlapDim = new UiRptMetaUserOlapDim();
		userOlapDim.setCustomRptId(Integer.valueOf(cusRptId));
		userOlapDim.setDimId("DF001");
		userOlapDim.setDisplayOrder(6);
		rptDims.add(userOlapDim);
		userOlapSrv.saveReportDim(cusRptId, rptDims);
	}

	@Test
	public void testGetCustomDimsNull() {
		String cusRptId = null;
		List<UiRptMetaUserOlapDim> userOlapDims = userOlapSrv
				.getCustomDims(cusRptId);
		assertNull(userOlapDims);
		cusRptId = "";
		assertNull(userOlapDims);
	}

	@Test
	public void testGetCustomDims() {
		String cusRptId = "10006";
		List<UiRptMetaUserOlapDim> userOlapDims = userOlapSrv
				.getCustomDims(cusRptId);
		assertNotNull(userOlapDims);
		assertEquals("DF001", userOlapDims.get(0).getDimId());
	}

	@Test
	public void testGetCusRptDims() {
		String cusRptId = "10006";
		Map<String, UiRptMetaUserOlapDim> userOlapDims = userOlapSrv
				.getCusRptDims(cusRptId);
		assertNotNull(userOlapDims);
		assertEquals("DF001", userOlapDims.get("DF001").getDimId());
	}

	@Test
	public void testDeleteReportDimNotInteger() {
		String cusRptId = "test";
		exception.expect(NumberFormatException.class);
		userOlapSrv.deleteReportDim(cusRptId);
	}

	@Test
	public void testDeleteReportDimNull() {
		String cusRptId = null;
		// 什么也不做
		userOlapSrv.deleteReportDim(cusRptId);
		cusRptId = "10006";
		List<UiRptMetaUserOlapDim> userOlapDims = userOlapSrv
				.getCustomDims(cusRptId);
		assertNotNull(userOlapDims);
		assertEquals("DF001", userOlapDims.get(0).getDimId());
	}

	@Test
	public void testDeleteReportDim() {
		String cusRptId = "10006";
		userOlapSrv.deleteReportDim(cusRptId);
	}
}
