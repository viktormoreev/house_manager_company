package org.example;

import org.example.configuration.SessionFactoryUtil;
import org.example.dao.*;
import org.example.entity.*;
import org.example.errors.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        SessionFactoryUtil.getSessionFactory().openSession();

        try {
            BuildingManagerDao.deleteBuildingManager(1L);
        } catch (BuildingManagerNotFoundException e) {
            throw new RuntimeException(e);
        }

    }


}