package org.example.mapper;

import org.example.dto.BuildingManagerDTO;
import org.example.dto.CompanyDTO;
import org.example.entity.BuildingManager;
import org.example.entity.Company;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class EntityMapper {
    private static ModelMapper modelMapper = new ModelMapper();

    public static CompanyDTO mapCompanyToCompanyDTO(Company company){
        return modelMapper.map(company, CompanyDTO.class);
    }

    public static BuildingManagerDTO mapBuildingManagerToDTO(BuildingManager buildingManager){
        return modelMapper.map(buildingManager, BuildingManagerDTO.class);
    }

    public static List<BuildingManagerDTO> mapBuildingManagerListToDTO(List<BuildingManager> buildingManagers){
        List<BuildingManagerDTO> buildingManagersDTO;
        buildingManagersDTO= buildingManagers.stream().map(buildingManager -> EntityMapper.mapBuildingManagerToDTO(buildingManager)).collect(Collectors.toList());
        return buildingManagersDTO;
    }

}
