package com.jwt.tok.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.jwt.tok.dao.SlqDao;
import com.jwt.tok.model.Slq;
import com.jwt.tok.model.Slq1;
import com.jwt.tok.model.Slq2;

@Service
public class SlqService {

	@Autowired
	private SlqDao dao;

	// HEADER
	@Autowired
	private JdbcTemplate jdbcTemplate;

//	public void saveSlq(Slq slq) {
//		dao.saveSlq(slq);
//	}
	public Slq saveSlq(Slq slq) {
		return dao.saveSlq(slq);
	}
	
	//quotation number display on frentend
	public String getNextDocNo(String fyear, String doctype) {
	    return dao.generateDocNo(fyear, doctype);
	}

	// ITEM
//	public void saveItem(Slq1 item) {
//		dao.saveSlq1("2025", "SLQ", item.getDocno(), item.getItemno(), item.getLoc(), item.getName1(), item.getWdhq());
//	}
	public Slq1 saveItem(Slq1 item) {
		return dao.saveSlq1Auto("2026", "SLQ", item.getDocno(), item);
	}

	// GET ITEMS
	public List<Map<String, Object>> getItems(String docno) {
		return dao.getItems(docno);
	}

	// DETAILS
	public List<Slq2> getItemDetails(String fyear, String doctype, String docno, String itemno) {
		return dao.getSlq2(fyear, doctype, docno, itemno);
	}

	// PROCESS
	public void processBom(String fyear, String doctype, String docno, String itemno, String uid) {
		dao.processSlq22(fyear, doctype, docno, itemno, uid);
	}

	// TOTALS
	public Map<String, Object> getTotals(String fyear, String doctype, String docno, String itemno) {
		return dao.getTotals(fyear, doctype, docno, itemno);
	}

	// DELETE
	public void deleteQuotation(String fyear, String doctype, String docno) {
		dao.deleteSlq(fyear, doctype, docno);
	}

	public Map<String, Object> saveAndCalculateSlq2(String fyear, String doctype, String docno, String itemno,
			List<Slq2> list) {

		// 1. DELETE OLD
		jdbcTemplate.update("DELETE FROM slq2 WHERE FYEAR=? AND DOCTYPE=? AND DOCNO=? AND ITEMNO=?", fyear, doctype,
				docno, itemno);

		// 2. INSERT NEW WITH AUTO SRNO
		for (Slq2 s : list) {

			String srno = dao.generateSrNo(fyear, doctype, docno, itemno);

			// ✅ BTYP FIX
			String type = s.getBomHardware().trim();

			if (type.startsWith("B")) {
				type = "B - Bom";
			} else if (type.startsWith("H")) {
				type = "H - Hardware";
			} else {
				throw new RuntimeException("Invalid Type");
			}

			// ✅ CODE VALIDATION
			if (type.equals("B - Bom")) {

				Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bom1 WHERE rid=?", Integer.class,
						s.getCode());

				if (count == null || count == 0) {
					throw new RuntimeException("Invalid BOM code: " + s.getCode());
				}

			} else {

				Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM mate WHERE code=?", Integer.class,
						s.getCode());

				if (count == null || count == 0) {
					throw new RuntimeException("Invalid Material code: " + s.getCode());
				}
			}

			// ✅ NULL SAFE
			String w = (s.getWidth() == null || s.getWidth().isEmpty()) ? "0" : s.getWidth();
			String h = (s.getHeight() == null || s.getHeight().isEmpty()) ? "0" : s.getHeight();
			String d = (s.getDepth() == null || s.getDepth().isEmpty()) ? "0" : s.getDepth();
			String q = (s.getQuantity() == null || s.getQuantity().isEmpty()) ? "1" : s.getQuantity();

			// ✅ INSERT
			jdbcTemplate.update(
					"INSERT INTO slq2 (FYEAR, DOCTYPE, DOCNO, ITEMNO, SRNO, BTYP, CODE, DES, W, H, D, QTY) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					fyear, doctype, docno, itemno, srno, type, s.getCode(), s.getDescription(), w, h, d, q);
		}

		// 3. CALL SP (🔥 MAIN CALCULATION)
		jdbcTemplate.update("CALL SPSLQ22(?,?,?,?,?)", fyear, doctype, docno, itemno, "999");
		
		//===============================k
		List<Slq2> slq2List = jdbcTemplate.query(
				"SELECT SRNO, BTYP, CODE, DES, W, H, D, QTY, AMT1, AMTCC, AMTCT, AMTDC, AMTDT "
						+ "FROM slq2 WHERE FYEAR=? AND DOCTYPE=? AND DOCNO=? AND ITEMNO=?",
				new Object[] { fyear, doctype, docno, itemno }, (rs, rowNum) -> {

					Slq2 s = new Slq2();

					s.setSrNo(rs.getString("SRNO"));
					s.setBomHardware(rs.getString("BTYP"));
					s.setCode(rs.getString("CODE"));
					s.setDescription(rs.getString("DES"));
					s.setWidth(rs.getString("W"));
					s.setHeight(rs.getString("H"));
					s.setDepth(rs.getString("D"));
					s.setQuantity(rs.getString("QTY"));

					s.setMaterialValue(rs.getString("AMT1"));
					s.setCustomerCash(rs.getString("AMTCC"));
					s.setCustomerTax(rs.getString("AMTCT"));
					s.setDealerCash(rs.getString("AMTDC"));
					s.setDealerTax(rs.getString("AMTDT"));

					return s;
				});

		// ===============================
		// 5. FETCH UPDATED SLQ1 (🔥 IMPORTANT)
		// ===============================
		Slq1 slq1 = jdbcTemplate.queryForObject(
				"SELECT * FROM slq1 WHERE fyear=? AND doctype=? AND docno=? AND itemno=?",
				new Object[] { fyear, doctype, docno, itemno }, (rs, rowNum) -> {

					Slq1 s = new Slq1();

					s.setFyear(rs.getString("fyear"));
					s.setDoctype(rs.getString("doctype"));
					s.setDocno(rs.getString("docno"));
					s.setItemno(rs.getString("itemno"));

					s.setAmt1(rs.getString("amt1"));
					s.setAmtcc(rs.getString("amtcc"));
					s.setAmtct(rs.getString("amtct"));
					s.setAmtdc(rs.getString("amtdc"));
					s.setAmtdt(rs.getString("amtdt"));
					s.setDca(rs.getString("dca"));
					s.setDda(rs.getString("dda"));

					return s;
				});

		// ===============================
		// 6. FINAL RESPONSE
		// ===============================
		Map<String, Object> result = new HashMap<>();
		result.put("items", slq2List);
		result.put("itemTotal", slq1);

		return result;
	}

	public List<Map<String, Object>> getAllQuotations(String fyear) {

		if (fyear == null || fyear.isEmpty()) {
			return jdbcTemplate.queryForList("CALL spslq38(NULL)");
		}

		return jdbcTemplate.queryForList("CALL spslq38(?)", fyear);
	}

	public Map<String, Object> calculateFinal(String fyear, String doctype, String docno, String type) {

		return jdbcTemplate.queryForMap("CALL SPSLQ26(?,?,?,?)", fyear, doctype, docno, type);
	}

	public List<String> getBomTypes() {
		return dao.getBomTypes();
	}

	public List<String> getCustomerTypes() {

		return Arrays.asList("CC - Customer Cash Quotation", "CT - Customer Tax Quotation",
				"DC - Dealer Cash Quotation", "DT - Dealer Tax Quotation");
	}

	public Slq1 getUpdatedSlq1(String fyear, String doctype, String docno, String itemno) {

		return jdbcTemplate.queryForObject("SELECT * FROM slq1 WHERE fyear=? AND doctype=? AND docno=? AND itemno=?",
				new Object[] { fyear, doctype, docno, itemno }, (rs, rowNum) -> {
					Slq1 s = new Slq1();

					s.setFyear(rs.getString("fyear"));
					s.setDoctype(rs.getString("doctype"));
					s.setDocno(rs.getString("docno"));
					s.setItemno(rs.getString("itemno"));

					s.setAmt1(rs.getString("amt1"));
					s.setAmtcc(rs.getString("amtcc"));
					s.setAmtct(rs.getString("amtct"));
					s.setAmtdc(rs.getString("amtdc"));
					s.setAmtdt(rs.getString("amtdt"));
					s.setDca(rs.getString("dca"));
					s.setDda(rs.getString("dda"));

					return s;
				});
	}

	public Map<String, Object> updateQuotation(String fyear, String doctype, String docno,
			Map<String, Object> payload) {

// 1. UPDATE HEADER
		jdbcTemplate.update("UPDATE slq SET t0=?, t1=?, t3=?, t6=? WHERE fyear=? AND doctype=? AND docno=?",
				payload.get("name"), payload.get("mobile"), payload.get("email"), payload.get("salesExecutive"), fyear,
				doctype, docno);

		List<Map<String, Object>> items = (List<Map<String, Object>>) payload.get("items");

		for (Map<String, Object> item : items) {

			String itemno = item.get("itemno").toString();

// 2. UPDATE slq1
			jdbcTemplate.update(
					"UPDATE slq1 SET loc=?, name1=?, wdhq=? WHERE fyear=? AND doctype=? AND docno=? AND itemno=?",
					item.get("loc"), item.get("name"), item.get("wdhq"), fyear, doctype, docno, itemno);

// 3. DELETE OLD slq2
			jdbcTemplate.update("DELETE FROM slq2 WHERE fyear=? AND doctype=? AND docno=? AND itemno=?", fyear, doctype,
					docno, itemno);

// 4. INSERT NEW slq2
			List<Map<String, Object>> details = (List<Map<String, Object>>) item.get("details");

			for (Map<String, Object> d : details) {

				String srno = dao.generateSrNo(fyear, doctype, docno, itemno);

				jdbcTemplate.update(
						"INSERT INTO slq2 (fyear, doctype, docno, itemno, srno, btyp, code, des, w, h, d, qty) "
								+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
						fyear, doctype, docno, itemno, srno, d.get("type"), d.get("code"), d.get("desc"), d.get("w"),
						d.get("h"), d.get("d"), d.get("qty"));
			}

// 🔥 5. PROCESS (MOST IMPORTANT)
			jdbcTemplate.update("CALL SPSLQ22(?,?,?,?,?)", fyear, doctype, docno, itemno, "999");
		}

// 🔥 6. FINAL TOTAL
		Map<String, Object> finalTotal = jdbcTemplate.queryForMap("CALL SPSLQ26(?,?,?,?)", fyear, doctype, docno, "CC");

		return finalTotal;
	}
	
	public Map<String, Object> getQuotation(String fyear, String doctype, String docno) {

	    Map<String, Object> result = new HashMap<>();

	    // 1. HEADER
	    Map<String, Object> header = jdbcTemplate.queryForMap(
	        "SELECT * FROM slq WHERE fyear=? AND doctype=? AND docno=?",
	        fyear, doctype, docno
	    );

	    // 2. ITEMS (slq1)
	    List<Map<String, Object>> items = jdbcTemplate.queryForList(
	        "SELECT * FROM slq1 WHERE fyear=? AND doctype=? AND docno=?",
	        fyear, doctype, docno
	    );

	    // 3. FOR EACH ITEM → slq2
	    for (Map<String, Object> item : items) {

	        String itemno = item.get("itemno").toString();

	        List<Map<String, Object>> details = jdbcTemplate.queryForList(
	            "SELECT * FROM slq2 WHERE fyear=? AND doctype=? AND docno=? AND itemno=?",
	            fyear, doctype, docno, itemno
	        );

	        item.put("details", details);
	    }

	    // 4. GRAND TOTAL
	    Map<String, Object> totals =
	            jdbcTemplate.queryForMap("CALL SPSLQ26(?,?,?,?)",
	                    fyear, doctype, docno, "CC");

	    result.put("header", header);
	    result.put("items", items);
	    result.put("totals", totals);

	    return result;
	}
}