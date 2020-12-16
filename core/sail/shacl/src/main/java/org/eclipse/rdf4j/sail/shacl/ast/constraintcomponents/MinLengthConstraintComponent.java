package org.eclipse.rdf4j.sail.shacl.ast.constraintcomponents;

import java.util.Set;
import java.util.function.Function;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.SHACL;
import org.eclipse.rdf4j.model.vocabulary.XMLSchema;
import org.eclipse.rdf4j.sail.shacl.SourceConstraintComponent;
import org.eclipse.rdf4j.sail.shacl.ast.planNodes.FilterPlanNode;
import org.eclipse.rdf4j.sail.shacl.ast.planNodes.MinLengthFilter;
import org.eclipse.rdf4j.sail.shacl.ast.planNodes.PlanNode;

public class MinLengthConstraintComponent extends SimpleAbstractConstraintComponent {

	long minLength;

	public MinLengthConstraintComponent(long minLength) {
		this.minLength = minLength;
	}

	@Override
	public void toModel(Resource subject, IRI predicate, Model model, Set<Resource> exported) {
		model.add(subject, SHACL.MIN_LENGTH,
				SimpleValueFactory.getInstance().createLiteral(minLength + "", XMLSchema.INTEGER));
	}

	@Override
	String getSparqlFilterExpression(String varName, boolean negated) {
		if (negated) {
			return "STRLEN(STR(?" + varName + ")) >= " + minLength;
		} else {
			return "STRLEN(STR(?" + varName + ")) < " + minLength;
		}
	}

	@Override
	Function<PlanNode, FilterPlanNode> getFilterAttacher() {
		return (parent) -> new MinLengthFilter(parent, minLength);
	}

	@Override
	public SourceConstraintComponent getConstraintComponent() {
		return SourceConstraintComponent.MinLengthConstraintComponent;
	}

	@Override
	public ConstraintComponent deepClone() {
		return new MinLengthConstraintComponent(minLength);
	}
}
