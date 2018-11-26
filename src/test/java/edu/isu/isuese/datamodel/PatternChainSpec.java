package edu.isu.isuese.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class PatternChainSpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        PatternChain chain = new PatternChain();
        a(chain).shouldBe("valid");
//        //a(chain.errors().get("author")).shouldBeEqual("Author must be provided");
        chain.set("chainKey", "chain");
        a(chain).shouldBe("valid");
        chain.save();
        chain = PatternChain.findById(1);
        a(chain.getId()).shouldNotBeNull();
        a(chain.get("chainKey")).shouldBeEqual("chain");
        a(PatternChain.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddPatternInstance() {
        PatternChain chain = PatternChain.createIt("chainKey", "chain");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        chain.addInstance(inst);
        chain.save();

        a(chain.getInstances().size()).shouldBeEqual(1);
    }

    @Test
    public void canRemovePatternInstance() {
        PatternChain chain = PatternChain.createIt("chainKey", "chain");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        chain.addInstance(inst);
        chain.save();

        a(chain.getInstances().size()).shouldBeEqual(1);
        chain = PatternChain.findById(1);
        chain.remove(inst);
        a(chain.getInstances().size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        PatternChain chain = PatternChain.createIt("chainKey", "chain");
        PatternInstance inst = PatternInstance.createIt("instKey", "inst");
        chain.addInstance(inst);
        chain.save();

        a(PatternChain.count()).shouldBeEqual(1);
        a(PatternInstance.count()).shouldBeEqual(1);
        chain.delete(true);
        a(PatternChain.count()).shouldBeEqual(0);
        a(PatternInstance.count()).shouldBeEqual(0);
    }
}
