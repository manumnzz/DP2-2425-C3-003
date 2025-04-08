
package acme.entities.maintenance;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceTask extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Optional
	@Valid
	@ManyToOne(optional = true)
	private Task				task;

	@Optional
	@Valid
	@ManyToOne(optional = true)
	private MaintenanceRecord	maintenanceRecord;

}
