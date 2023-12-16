package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.BuildingManager;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingManagerDTO {
    private Long id;
    private String name;

    @Override
    public String toString() {
        return "BuildingManagerDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
