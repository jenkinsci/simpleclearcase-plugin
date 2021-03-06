/**
 * The MIT License
 * 
 * Copyright (c) 2011, Sun Microsystems, Inc., Sam Tavakoli
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package jenkins.plugins.simpleclearcase;

import hudson.model.AbstractBuild;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jenkins.plugins.simpleclearcase.util.DateUtil;

public class SimpleClearCaseChangeLogSet extends hudson.scm.ChangeLogSet<SimpleClearCaseChangeLogEntry> {
    private List<SimpleClearCaseChangeLogEntry> entries;

    protected SimpleClearCaseChangeLogSet(AbstractBuild<?, ?> build, 
                                                            List<SimpleClearCaseChangeLogEntry> entries) {
        super(build);
        this.entries = entries;

        for (SimpleClearCaseChangeLogEntry entry : entries) {
            entry.setParent(this);
        }
    }

    /**
     * @param loadRules
     * @return a LoadRuleDateMap which maps a load rule against the latest commit date of that 
     *         specific load rule
     */
    public LoadRuleDateMap getLatestCommitDates(List<String> loadRules) {
        LoadRuleDateMap ret = new LoadRuleDateMap();

        for (String lr : loadRules) {
            ret.setBuildTime(lr, getLatestCommitDate(lr));
        }
        return ret;
    }

    /**
     * @param loadRule
     * @return the latest commit date from all of the entries who are fetched from load rule 'loadrule'
     */
    private Date getLatestCommitDate(String loadRule) {
        List<SimpleClearCaseChangeLogEntry> prefixedEntries = 
                                                           new ArrayList<SimpleClearCaseChangeLogEntry>();

        for (SimpleClearCaseChangeLogEntry entry : entries) {
            if (entry.containsPathWithPrefix(loadRule) == true) {
                prefixedEntries.add(entry);
            }
        }
        return DateUtil.getLatestDate(prefixedEntries);
    }

    public Iterator<SimpleClearCaseChangeLogEntry> iterator() {
        return entries.iterator();
    }

    public List<SimpleClearCaseChangeLogEntry> getEntries() {
        return entries;
    }

    @Override
    public boolean isEmptySet() {
        return entries.isEmpty();
    }
}
