package com.jwt.tok.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jwt.tok.exception.ApiException;
import com.jwt.tok.model.FormulaHead;
import com.jwt.tok.model.Material;
import com.jwt.tok.model.QuantitySurvey;
import com.jwt.tok.model.QuantitySurveyItem;
import com.jwt.tok.model.SideType;
import com.jwt.tok.repository.FormulaHeadRepository;
import com.jwt.tok.repository.MaterialRepository;
import com.jwt.tok.repository.QuantitySurveyRepository;
import com.jwt.tok.repository.SideTypeRepository;

@Service
public class QuantitySurveyService {

	private final QuantitySurveyRepository repo;
	private final MaterialRepository materialRepo;

	private final FormulaHeadRepository formulaRepo;
	private final SideTypeRepository sideRepo;

	public QuantitySurveyService(QuantitySurveyRepository repo, MaterialRepository materialRepo,
			FormulaHeadRepository formulaRepo, SideTypeRepository sideRepo) {
		this.repo = repo;
		this.materialRepo = materialRepo;
		this.formulaRepo = formulaRepo;
		this.sideRepo = sideRepo;
	}

	public QuantitySurvey save(QuantitySurvey survey) {

		if (survey.getName() == null || survey.getName().isEmpty()) {
			throw new ApiException("Name is required");
		}

		if (survey.getSurveyNo() == null || survey.getSurveyNo().trim().isEmpty()) {
			throw new ApiException("Survey No is required");
		}

		if (survey.getItems() != null) {
			for (QuantitySurveyItem item : survey.getItems()) {

				if (item.getMaterialId() != null) {

					Material mat = materialRepo.findById(item.getMaterialId())
							.orElseThrow(() -> new ApiException("Material not found"));

					item.setCode(mat.getCode());
					item.setType(mat.getTyp());
					item.setGrp(mat.getGrp());
					item.setDes(mat.getDes());
				}

				if (item.getFormulaId() != null) {

					FormulaHead formula = formulaRepo.findById(item.getFormulaId())
							.orElseThrow(() -> new ApiException("Formula not found"));

					item.setFormula(formula.getDescription());
				}

				if (item.getSideId() != null) {

					SideType side = sideRepo.findById(item.getSideId())
							.orElseThrow(() -> new ApiException("Side not found"));

					item.setSide(side.getDescription());
				}
				if (survey.getSurveyNo() == null || survey.getSurveyNo().isEmpty()) {

					Long maxSurveyNo = repo.findMaxSurveyNo();

					if (maxSurveyNo == null) {
						survey.setSurveyNo("101"); // first
					} else {
						survey.setSurveyNo(String.valueOf(maxSurveyNo + 1));
					}
				}

				item.setSurvey(survey);
			}
		}

		return repo.save(survey);
	}

	public QuantitySurvey update(Long id, QuantitySurvey updated) {

		QuantitySurvey existing = getById(id);

		existing.setQuantity(updated.getQuantity());
		existing.setName(updated.getName());
		existing.setHeight(updated.getHeight());
		existing.setDepth(updated.getDepth());
		existing.setWidth(updated.getWidth());

		// 🔥 FIX
		if (existing.getItems() != null) {
			existing.getItems().clear();
		} else {
			existing.setItems(new ArrayList<>());
		}

//        for (QuantitySurveyItem item : updated.getItems()) {
//
//            if (item.getMaterialId() != null) {
//
//                Material mat = materialRepo.findById(item.getMaterialId())
//                        .orElseThrow(() -> new ApiException("Material not found"));
//
//                item.setCode(mat.getCode());
//                item.setType(mat.getTyp());
//                item.setGrp(mat.getGrp());
//            }
//
//            item.setSurvey(existing);
//            existing.getItems().add(item);
//        }
		for (QuantitySurveyItem item : updated.getItems()) {

			// 🔥 SideType
			if (item.getSideId() != null) {

				SideType side = sideRepo.findById(item.getSideId())
						.orElseThrow(() -> new ApiException("Side not found"));

				item.setSide(side.getDescription());
			}

			// 🔥 Material
			if (item.getMaterialId() != null) {

				Material mat = materialRepo.findById(item.getMaterialId())
						.orElseThrow(() -> new ApiException("Material not found"));

				item.setCode(mat.getCode());
				item.setType(mat.getTyp());
				item.setGrp(mat.getGrp());
				item.setDes(mat.getDes());
			}

			// 🔥 Formula
			if (item.getFormulaId() != null) {

				FormulaHead formula = formulaRepo.findById(item.getFormulaId())
						.orElseThrow(() -> new ApiException("Formula not found"));

				item.setFormula(formula.getDescription());
			}

			item.setSurvey(existing);
			existing.getItems().add(item);
		}

		return repo.save(existing);
	}

	public List<QuantitySurvey> getAll() {
		return repo.findAll();
	}

	public QuantitySurvey getById(Long id) {
		return repo.findById(id).orElseThrow(() -> new ApiException("Survey not found"));
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public Long getMaxSurveyNo() {
		return repo.findMaxSurveyNo();
	}
}