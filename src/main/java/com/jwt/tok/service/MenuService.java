package com.jwt.tok.service;

import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import com.jwt.tok.model.Menu;
import com.jwt.tok.repository.MenuRepository;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepo;

    public MenuService(MenuRepository menuRepo) {
        this.menuRepo = menuRepo;
    }

    public Menu createMenu(Menu menu) {
        return menuRepo.save(menu);
    }

    public List<Menu> getAllMenus() {
        return menuRepo.findAll();
    }

    public Menu getMenu(Long id) {
        return menuRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu not found with id: " + id));
    }

    public Menu updateMenu(Long id, Menu menu) {
        Menu dbMenu = getMenu(id);
        dbMenu.setMenuName(menu.getMenuName());
        dbMenu.setDescription(menu.getDescription());
        return menuRepo.save(dbMenu);
    }

    public void deleteMenu(Long id) {
        if (!menuRepo.existsById(id)) {
            throw new RuntimeException("Menu not found with id: " + id);
        }
        menuRepo.deleteById(id);
    }
}
