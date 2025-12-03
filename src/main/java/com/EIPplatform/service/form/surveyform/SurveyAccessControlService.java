package com.EIPplatform.service.form.surveyform;

import com.EIPplatform.exception.ExceptionFactory;
import com.EIPplatform.exception.errorCategories.ForbiddenError;
import com.EIPplatform.exception.errorCategories.UserError;
import com.EIPplatform.model.entity.user.authentication.Role;
import com.EIPplatform.model.entity.user.authentication.UserAccount;
import com.EIPplatform.model.enums.RoleName;
import com.EIPplatform.repository.authentication.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyAccessControlService implements SurveyAccessControlServiceInterface {

    private final UserAccountRepository userAccountRepository;
    private final ExceptionFactory exceptionFactory;

    @Override
    public void ensureBecamexRole(UUID userAccountId) {
        UserAccount user = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> exceptionFactory.createNotFoundException("User", "id", userAccountId, UserError.NOT_FOUND));

        boolean isBecamex = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == RoleName.BECAMEX);

        if (!isBecamex) {
            throw exceptionFactory.createCustomException(ForbiddenError.FORBIDDEN);
        }
    }
}
