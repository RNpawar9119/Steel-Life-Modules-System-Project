package com.jwt.tok.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Product;
import com.jwt.tok.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository repo;
	private final ObjectMapper mapper = new ObjectMapper();

	@Value("${file.upload-dir}")
	private String uploadDir;

	public ProductService(ProductRepository repo) {
		this.repo = repo;
	}

	/* ================= CREATE ================= */
	public Product createProduct(Product product) {

		if (product.getProductName() == null || product.getProductName().isEmpty()) {
			throw new ApiException("Product name required");
		}

		product.setProductCode(generateProductCode());

		if (product.getHns() != null && product.getHns().getTax() != null) {
			product.setTax(product.getHns().getTax().getTaxPercentage());
		}

		return repo.save(product);
	}

	private String generateProductCode() {
		Product last = repo.findTopByOrderByIdDesc();
		if (last == null)
			return "PRD-0001";

		int num = Integer.parseInt(last.getProductCode().split("-")[1]);
		return String.format("PRD-%04d", num + 1);
	}

	/* ================= UPLOAD IMAGES ================= */
	public Product uploadImages(Long id, MultipartFile i1, MultipartFile i2, MultipartFile i3) throws IOException {

		Product p = repo.findById(id).orElseThrow(() -> new ApiException("Product not found"));

		Path dir = Paths.get(uploadDir, "products");
		Files.createDirectories(dir);

		if (i1 != null && !i1.isEmpty())
			p.setImage1(save(dir, i1));
		if (i2 != null && !i2.isEmpty())
			p.setImage2(save(dir, i2));
		if (i3 != null && !i3.isEmpty())
			p.setImage3(save(dir, i3));

		return repo.save(p);
	}

	private String save(Path dir, MultipartFile file) throws IOException {
		String name = System.currentTimeMillis() + "_" + file.getOriginalFilename().replaceAll("\\s+", "_");
		Files.copy(file.getInputStream(), dir.resolve(name));
		return "/uploads/products/" + name;
	}

	/* ================= GET ================= */
	public Product getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ApiException("Product not found"));
	}

	public List<Product> getAll() {
		return repo.findAll();
	}

	/* ================= UPDATE ================= */
	public Product updateProduct(Long id, String json, MultipartFile i1, MultipartFile i2, MultipartFile i3)
			throws IOException {

		Product p = getById(id);
		Product u = mapper.readValue(json, Product.class);

		p.setProductName(u.getProductName());
		p.setModelName(u.getModelName());
		p.setDescription(u.getDescription());
		p.setUnit(u.getUnit());
		p.setType(u.getType());
		p.setSalesPrice(u.getSalesPrice());
		p.setDealerPrice(u.getDealerPrice());
		p.setBuyPrice(u.getBuyPrice());
		p.setHns(u.getHns());

		if (u.getHns() != null && u.getHns().getTax() != null) {
			p.setTax(u.getHns().getTax().getTaxPercentage());
		}

		return uploadImages(id, i1, i2, i3);
	}

	/* ================= DELETE ================= */
	public void deleteProduct(Long id) {
		Product p = getById(id);
		deleteFile(p.getImage1());
		deleteFile(p.getImage2());
		deleteFile(p.getImage3());
		repo.delete(p);
	}

	private void deleteFile(String path) {
		if (path == null)
			return;
		try {
			Files.deleteIfExists(Paths.get(uploadDir, path.replace("/uploads/", "")));
		} catch (Exception ignored) {
		}
	}

	public List<Product> searchProduct(String q) {
		if (q == null || q.trim().isEmpty())
			return repo.findAll();
		return repo.findByProductCodeContainingIgnoreCaseOrProductNameContainingIgnoreCase(q, q);
	}

	public String getNextProductCode() {
		return generateProductCode();
	}

	public List<Map<String, Object>> searchProductLite(String q) {

	    if (q == null || q.trim().isEmpty()) {
	        throw new ApiException("Search keyword required");
	    }

	    return repo.searchProductLite(q);
	}

}
