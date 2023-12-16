package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Building;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuildingDTO {
    private Long id;
    private int floors;
    private String address ;
    public BuildingDTO(Building building) {
        this.id = building.getId();
        this.floors = building.getFloors();
        this.address = building.getAddress();
    }
    @Override
    public String toString() {
        return "BuildingDTO{" +
                "id=" + id +
                ", floors=" + floors +
                ", address='" + address + '\'' +
                '}';
    }
}
