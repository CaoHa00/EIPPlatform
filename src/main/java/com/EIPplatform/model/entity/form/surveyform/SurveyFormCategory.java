package com.EIPplatform.model.entity.form.surveyform;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "forms_survey_form_category")
public class SurveyFormCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "surveyFormCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuestionCategory> questionCategories = new HashSet<>();

    @OneToMany(mappedBy = "surveyFormCategory", fetch = FetchType.LAZY)
    private Set<SurveyForm> surveyForms = new HashSet<>();
}
