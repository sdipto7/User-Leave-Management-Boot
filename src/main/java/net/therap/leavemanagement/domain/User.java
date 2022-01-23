package net.therap.leavemanagement.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static net.therap.leavemanagement.domain.Designation.*;

/**
 * @author rumi.dipto
 * @since 11/22/21
 */
@Getter
@Setter
@Entity
@Table(name = "lm_user")
public class User extends Persistent {

    private static final long serialVersionUID = 1L;

    @Column(name = "first_name")
    @Size(min = 2, max = 100)
    @NotNull
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2, max = 100)
    @NotNull
    private String lastName;

    @Size(min = 2, max = 100)
    @NotNull
    private String username;

    @Size(min = 5, max = 100)
    @NotNull
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Designation designation;

    @DecimalMin(value = "5000.00")
    @Digits(integer = 10, fraction = 2)
    @NotNull
    private BigDecimal salary;

    private boolean activated;

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public boolean isHrExecutive() {
        return this.designation.equals(HR_EXECUTIVE);
    }

    public boolean isTeamLead() {
        return this.designation.equals(TEAM_LEAD);
    }

    public boolean isDeveloper() {
        return this.designation.equals(DEVELOPER);
    }

    public boolean isTester() {
        return this.designation.equals(TESTER);
    }
}
