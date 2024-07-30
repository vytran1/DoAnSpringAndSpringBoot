package com.shopme.setting;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.setting.Setting;
import com.shopme.common.entity.setting.SettingCategory;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class SettingRepositoryTest {
     @Autowired
     private SettingRepository settingRepository;
     
     @Test
     public void testFindByTwoCategory() {
    	List<Setting> settingResults = settingRepository.findByTwoCategory(SettingCategory.GENERAL, SettingCategory.CURRENCY);
    	settingResults.forEach((x)->System.out.println(x));
     }
     
}
