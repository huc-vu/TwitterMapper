package filters;

import twitter4j.Status;

import java.util.List;

public class OrFilter implements Filter{
    private final Filter child1;
    private final Filter child2;

    public OrFilter(Filter child1, Filter child2) {
        this.child1 = child1;
        this.child2 = child2;
    }

    /**
     * An Or filter matches when one of its child does
     * @param s     the tweet to check
     * @return      whether or not it matches
     */
    @Override
    public boolean matches(Status s) {
        return child1.matches(s) || child2.matches(s);
    }

    @Override
    public List<String> terms() {
        List<String> res = child1.terms();
        res.addAll(child2.terms());
        return res;
    }

    public String toString() {
        return "(" + child1.toString() + " or " + child2.toString() + ")";
    }
}
