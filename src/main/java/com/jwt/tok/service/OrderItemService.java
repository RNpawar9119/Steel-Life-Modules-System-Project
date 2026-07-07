package com.jwt.tok.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.Dealer;
import com.jwt.tok.model.OrderItem;
import com.jwt.tok.model.Orders;
import com.jwt.tok.model.Sequence;
import com.jwt.tok.repository.DealerRepository;
import com.jwt.tok.repository.OrderItemRepository;
import com.jwt.tok.repository.OrderRepository;
import com.jwt.tok.repository.SequenceRepository;

@Service
public class OrderItemService {

	private final OrderItemRepository itemRepo;
	private final OrderRepository orderRepo;
	private final DealerRepository dealerRepo;

	private final SequenceRepository sequenceRepo;

	@Value("${file.upload-dir}")
	private String uploadDir;

	public OrderItemService(OrderItemRepository itemRepo, OrderRepository orderRepo, DealerRepository dealerRepo,
			SequenceRepository sequenceRepo) {
		this.itemRepo = itemRepo;
		this.orderRepo = orderRepo;
		this.dealerRepo = dealerRepo;
		this.sequenceRepo = sequenceRepo;

	}

	public OrderItem saveItem(Long orderId, String room, String product, Long sequenceId, MultipartFile doc1,
			MultipartFile doc2, MultipartFile doc3, MultipartFile doc4, MultipartFile doc5) throws Exception {

		Orders order = orderRepo.findById(orderId)
				.orElseThrow(() -> new ApiException("Order not found with id: " + orderId));

		OrderItem item = new OrderItem();
		if (sequenceId != null) {
			Sequence seq = sequenceRepo.findById(sequenceId).orElseThrow(() -> new ApiException("Sequence not found"));
			item.setSequence(seq);
		}

		item.setRoom(room);
		item.setProduct(product);
		item.setOrder(order);

		String itemDir = uploadDir + "/orders/items";
		Files.createDirectories(Paths.get(itemDir));

		item.setDoc1(saveFile(doc1));
		item.setDoc2(saveFile(doc2));
		item.setDoc3(saveFile(doc3));
		item.setDoc4(saveFile(doc4));
		item.setDoc5(saveFile(doc5));

		return itemRepo.save(item);
	}

	private String saveFile(MultipartFile file) throws Exception {
		if (file == null || file.isEmpty())
			return null;

		String itemDir = uploadDir + "/orders/items";

		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

		Files.copy(file.getInputStream(), Paths.get(itemDir, fileName), StandardCopyOption.REPLACE_EXISTING);

		return "/uploads/orders/items/" + fileName;
	}

	public void deleteOrderItem(Long itemId) {
		OrderItem item = itemRepo.findById(itemId)
				.orElseThrow(() -> new ApiException("Order item not found with id: " + itemId));

		itemRepo.delete(item);
	}

	public OrderItem updateItem(Long itemId, String room, String product, Long sequenceId, MultipartFile doc1,
			MultipartFile doc2, MultipartFile doc3, MultipartFile doc4, MultipartFile doc5) throws Exception {

		OrderItem item = itemRepo.findById(itemId).orElseThrow(() -> new ApiException("Order item not found"));

		item.setRoom(room);
		item.setProduct(product);
		// ⭐ ADD THIS BLOCK (Sequence update logic)
		if (sequenceId != null) {
			Sequence seq = sequenceRepo.findById(sequenceId).orElseThrow(() -> new ApiException("Sequence not found"));
			item.setSequence(seq);
		} else {
			item.setSequence(null); // optional (remove sequence if not selected)
		}
		if (doc1 != null && !doc1.isEmpty())
			item.setDoc1(saveFile(doc1));
		if (doc2 != null && !doc2.isEmpty())
			item.setDoc2(saveFile(doc2));
		if (doc3 != null && !doc3.isEmpty())
			item.setDoc3(saveFile(doc3));
		if (doc4 != null && !doc4.isEmpty())
			item.setDoc4(saveFile(doc4));
		if (doc5 != null && !doc5.isEmpty())
			item.setDoc5(saveFile(doc5));

		return itemRepo.save(item);
	}

	public void assignDealer(Long itemId, String dealerCode) {

		OrderItem item = itemRepo.findById(itemId).orElseThrow(() -> new RuntimeException("Order Item not found"));

		Dealer dealer = dealerRepo.findByDealerCode(dealerCode)
				.orElseThrow(() -> new RuntimeException("Dealer not found"));

		item.setDealer(dealer);
		itemRepo.save(item);
	}
	
	public List<Map<String, Object>> getBarcodePages(String pdfPath) {

	    List<Map<String, Object>> pages = new ArrayList<>();

	    try {

	        // remove /uploads from path
	        String relativePath = pdfPath.replace("/uploads", "");

	        // full path बनव
	        String fullPath = uploadDir + relativePath;

	        File file = new File(fullPath);

	        if (!file.exists()) {
	            throw new RuntimeException("PDF file not found: " + fullPath);
	        }

	        PDDocument document = PDDocument.load(file);

	        PDFRenderer renderer = new PDFRenderer(document);

	        int totalPages = document.getNumberOfPages();

	        for (int i = 0; i < totalPages; i++) {

	            BufferedImage image = renderer.renderImageWithDPI(i, 200);

	            String imageName = "barcode_" + System.currentTimeMillis() + "_" + (i + 1) + ".png";

	            String imagePath = uploadDir + "/barcode_pages/" + imageName;

	            File outputFile = new File(imagePath);

	            outputFile.getParentFile().mkdirs();

	            ImageIO.write(image, "png", outputFile);

	            Map<String, Object> pageData = new HashMap<>();

	            pageData.put("pageNumber", i + 1);

	            pageData.put("imageUrl", "/uploads/barcode_pages/" + imageName);

	            pages.add(pageData);
	        }

	        document.close();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return pages;
	} 
}