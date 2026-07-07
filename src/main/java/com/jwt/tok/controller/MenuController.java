package com.jwt.tok.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.jwt.tok.model.Menu;
import com.jwt.tok.service.MenuService;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    @PreAuthorize("hasAnyRole('Admin')")
    @PostMapping
    public Menu create(@RequestBody Menu menu) {
        return menuService.createMenu(menu);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping
    public List<Menu> all() {
        return menuService.getAllMenus();
    }
 
    @PreAuthorize("hasAnyRole('Admin')")
    @GetMapping("/{id}")
    public Menu get(@PathVariable Long id) {
        return menuService.getMenu(id);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @PutMapping("/{id}")
    public Menu update(@PathVariable Long id, @RequestBody Menu menu) {
        return menuService.updateMenu(id, menu);
    }

    @PreAuthorize("hasAnyRole('Admin')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
    }
}
