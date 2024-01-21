package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Company;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Long id;
    private String name;

    public CompanyDTO(Company company) {
        this.id = company.getId();
        this.name = company.getName();
    }
    public static CompanyDTO CompanyToDTO(Company company){
        CompanyDTO companyDTO=new CompanyDTO(company);
        return companyDTO;
    }
    @Override
    public String toString() {
        return "CompanyDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
