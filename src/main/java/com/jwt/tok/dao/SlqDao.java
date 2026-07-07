package com.jwt.tok.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jwt.tok.model.Slq;
import com.jwt.tok.model.Slq1;
import com.jwt.tok.model.Slq2;

@Repository
public class SlqDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public String generateDocNo(String fyear, String doctype) {

		String sql = "SELECT IFNULL(MAX(CAST(docno AS UNSIGNED)),0)+1 FROM slq WHERE fyear=? AND doctype=?";

		return jdbcTemplate.queryForObject(sql, String.class, fyear, doctype);
	}

	public Slq saveSlq(Slq slq) {

		String fyear = "2026";
		String doctype = "SLQ";

		// 🔥 generate docno
		String docno = generateDocNo(fyear, doctype);

		String sql = "INSERT INTO slq "
				+ "(fyear, doctype, docno, docdt, t0, t1, t2, t3, t4, t5, famt, t6, t7, t8, t9) "
				+ "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, fyear, doctype, docno, slq.getT0(), slq.getT1(), slq.getT2(), slq.getT3(), slq.getT4(),
				slq.getT5(), slq.getFamt(), slq.getT6(), slq.getT7(), slq.getT8(), slq.getT9());

		// 🔥 set generated docno
		slq.setDocno(docno);
		slq.setFyear(fyear);
		slq.setDoctype(doctype);

		return slq;
	}

//------------------------------------------------------------------------

	public String generateItemNo(String fyear, String doctype, String docno) {

		String sql = "SELECT IFNULL(MAX(CAST(itemno AS UNSIGNED)),0)+1 "
				+ "FROM slq1 WHERE fyear=? AND doctype=? AND docno=?";

		return jdbcTemplate.queryForObject(sql, String.class, fyear, doctype, docno);
	}

	// -----------------------------------------------------------------------

//	public Slq1 saveSlq1Auto(String fyear, String doctype, String docno, Slq1 item) {
//
//// 🔥 generate itemno
//		String itemno = generateItemNo(fyear, doctype, docno);
//
//// 🔥 call SP
//		jdbcTemplate.update("CALL SPSLQ20(?,?,?,?,?,?,?)", fyear, doctype, docno, itemno, item.getLoc(),
//				item.getName1(), item.getWdhq());
//
//// 🔥 set back in object
//		item.setItemno(itemno);
//		item.setDocno(docno);
//
//		return item;
//	}

	public Slq1 saveSlq1Auto(String fyear, String doctype, String docno, Slq1 item) {

		String itemno = generateItemNo(fyear, doctype, docno);

		jdbcTemplate.update("CALL SPSLQ20(?,?,?,?,?,?,?)", fyear, doctype, docno, itemno, item.getLoc(),
				item.getName1(), item.getWdhq());

		// 🔥 FETCH BACK FROM DB (IMPORTANT FIX)
		return jdbcTemplate.queryForObject("SELECT * FROM slq1 WHERE fyear=? AND doctype=? AND docno=? AND itemno=?",
				new Object[] { fyear, doctype, docno, itemno }, (rs, rowNum) -> {
					Slq1 s = new Slq1();
					s.setFyear(rs.getString("fyear"));
					s.setDoctype(rs.getString("doctype"));
					s.setDocno(rs.getString("docno"));
					s.setItemno(rs.getString("itemno"));
					s.setLoc(rs.getString("loc"));
					s.setName1(rs.getString("name1"));
					s.setWdhq(rs.getString("wdhq"));
					return s;
				});
	}

	// ------------------------------------------------------------------------------

	// get items
	public List<Map<String, Object>> getItems(String docno) {
		return jdbcTemplate.queryForList("SELECT * FROM slq1 WHERE docno = ?", docno);
	}

	// ---------------------------------------------------------------------------------
	// details
	public List<Slq2> getSlq2(String fyear, String doctype, String docno, String itemno) {

		return jdbcTemplate.query("CALL SPSLQ21(?,?,?,?)", new Object[] { fyear, doctype, docno, itemno },
				(rs, rowNum) -> {

					Slq2 obj = new Slq2();

					obj.setSrNo(rs.getString("Sr. NO."));
					obj.setBomHardware(rs.getString("Bom-Hardware"));
					obj.setCode(rs.getString("Code"));
					obj.setDescription(rs.getString("Description"));
					obj.setWidth(rs.getString("Width"));
					obj.setHeight(rs.getString("Height"));
					obj.setDepth(rs.getString("Depth"));
					obj.setQuantity(rs.getString("Quantity"));

					return obj;
				});
	}

	// ----------------------------------------------------------------------

	// process call
	public void processSlq22(String fyear, String doctype, String docno, String itemno, String uid) {

		jdbcTemplate.update("CALL SPSLQ22(?,?,?,?,?)", fyear, doctype, docno, itemno, uid);
	}

	// ------------------------------------------------------------------------
	// totals
	public Map<String, Object> getTotals(String fyear, String doctype, String docno, String itemno) {

		return jdbcTemplate.queryForMap("CALL SPSLQ25(?,?,?,?,?)", fyear, doctype, docno, itemno, "1");
	}

	// ------------------------------------------------------------------------

	// delete
	public void deleteSlq(String fyear, String doctype, String docno) {
		jdbcTemplate.update("CALL SPDEL00(?,?,?)", fyear, doctype, docno);
	}

	// ---------------------------------------------------------------------
	public String generateSrNo(String fyear, String doctype, String docno, String itemno) {

		String sql = "SELECT IFNULL(MAX(CAST(srno AS UNSIGNED)),0)+1 "
				+ "FROM slq2 WHERE fyear=? AND doctype=? AND docno=? AND itemno=?";

		return jdbcTemplate.queryForObject(sql, String.class, fyear, doctype, docno, itemno);
	}

	// ----------------------------------------------------------------------------

	public void insertSlq2(String fyear, String doctype, String docno, String itemno, Slq2 s) {

		String sql = "INSERT INTO slq2 " + "(FYEAR, DOCTYPE, DOCNO, ITEMNO, SRNO, BTYP, code, DES, W, H, D, qty, "
				+ "amt1, amtcc, amtct, amtdc, amtdt) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql, fyear, doctype, docno, itemno, s.getSrNo(), s.getBomHardware(), s.getCode(),
				s.getDescription(), s.getWidth(), s.getHeight(), s.getDepth(), s.getQuantity(),

				// calculated values
				s.getMaterialValue(), s.getCustomerCash(), s.getCustomerTax(), s.getDealerCash(), s.getDealerTax());
	}

	// --------------------------------------------------------------------------------

	public List<Map<String, Object>> getAllQuotations(String fyear) {

		return jdbcTemplate.queryForList("CALL spslq38(?)", fyear);
	}

	// ------------B-Bom & H-Hardware -logic-----------------------------
	public List<String> getBomTypes() {

		return jdbcTemplate.queryForList("SELECT 'B - Bom' AS Type UNION ALL SELECT 'H - Hardware'", String.class);
	}
	
	//--------------image data doc no-----------
	public boolean isDocNoExist(String docno) {

	    String sql = "SELECT COUNT(*) FROM slq WHERE docno = ?";

	    Integer count = jdbcTemplate.queryForObject(sql, Integer.class, docno);

	    return count != null && count > 0;
	}
}