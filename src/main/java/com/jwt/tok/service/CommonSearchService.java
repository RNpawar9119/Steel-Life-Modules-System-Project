package com.jwt.tok.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwt.tok.model.Bom1;
import com.jwt.tok.model.Material;
import com.jwt.tok.repository.Bom1Repository;
import com.jwt.tok.repository.MaterialRepository;

@Service
public class CommonSearchService {

    @Autowired
    private Bom1Repository bomRepository;

    @Autowired
    private MaterialRepository materialRepository;

    public List<String> searchCombined(String type, String query) {

        List<String> result = new ArrayList<>();

        // 🔥 BOM
        if ("B - Bom".equalsIgnoreCase(type)) {

            List<Bom1> bomList = bomRepository.searchBom(query);

            for (Bom1 b : bomList) {
                result.add(b.getRid() + "~" + b.getDes());
            }

        } else {
            // 🔥 HARDWARE

            List<Material> matList = materialRepository.searchHardware(query);

            for (Material m : matList) {
                result.add(m.getCode() + "~" + m.getDes());
            }
        }

        return result;
    }
}