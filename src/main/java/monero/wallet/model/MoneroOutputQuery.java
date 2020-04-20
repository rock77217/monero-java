package monero.wallet.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import common.types.Filter;
import common.utils.GenUtils;
import monero.daemon.model.MoneroKeyImage;
import monero.daemon.model.MoneroTx;

/**
 * Configures a query to retrieve wallet outputs (i.e. outputs that the wallet has or had the
 * ability to spend).
 * 
 * All outputs are returned except those that do not meet the criteria defined in this query.
 */
public class MoneroOutputQuery extends MoneroOutputWallet implements Filter<MoneroOutputWallet> {

  private MoneroTxQuery txQuery;
  private List<Integer> subaddressIndices;
  private BigInteger minAmount;
  private BigInteger maxAmount;
  private static MoneroOutputWallet EMPTY_OUTPUT = new MoneroOutputWallet();
  
  public MoneroOutputQuery() {
    super();
  }
  
  public MoneroOutputQuery(final MoneroOutputQuery query) {
    super(query);
    if (query.getMinAmount() != null) this.minAmount = query.getMinAmount();
    if (query.getMaxAmount() != null) this.maxAmount = query.getMaxAmount();
    if (query.subaddressIndices != null) this.subaddressIndices = new ArrayList<Integer>(query.subaddressIndices);
    this.txQuery = query.txQuery;  // reference original by default, MoneroTxQuery's deep copy will set this to itself
  }
  
  public MoneroOutputQuery copy() {
    return new MoneroOutputQuery(this);
  }
  
  public BigInteger getMinAmount() {
    return minAmount;
  }

  public void setMinAmount(BigInteger minAmount) {
    this.minAmount = minAmount;
  }

  public BigInteger getMaxAmount() {
    return maxAmount;
  }

  public void setMaxAmount(BigInteger maxAmount) {
    this.maxAmount = maxAmount;
  }

  @JsonIgnore
  public MoneroTxQuery getTxQuery() {
    return txQuery;
  }

  public MoneroOutputQuery setTxQuery(MoneroTxQuery txQuery) {
    this.txQuery = txQuery;
    return this;
  }
  
  public List<Integer> getSubaddressIndices() {
    return subaddressIndices;
  }

  public MoneroOutputQuery setSubaddressIndices(List<Integer> subaddressIndices) {
    this.subaddressIndices = subaddressIndices;
    return this;
  }
  
  public MoneroOutputQuery setSubaddressIndices(Integer... subaddressIndices) {
    this.subaddressIndices = GenUtils.arrayToList(subaddressIndices);
    return this;
  }
  
  /**
   * Indicates if the this query will fetch locked outputs, unlocked outputs, or both (null).
   * 
   * @return true if locked outputs queried, false of unlocked outputs queried, null if both
   */
  @JsonProperty("isLocked")
  @Override
  public Boolean isLocked() {
    if (txQuery == null) return null;
    return txQuery.isLocked();
  }
  
  /**
   * Convenience method to query outputs by the locked state of their tx.
   * 
   * @param isLocked specifies if the output's tx must be locked or unlocked (optional)
   * @return this query for chaining
   */
  public MoneroOutputQuery setIsLocked(Boolean isLocked) {
    if (txQuery == null) txQuery = new MoneroTxQuery();
    txQuery.setIsLocked(isLocked);
    return this;
  }
  
  @Override
  public boolean meetsCriteria(MoneroOutputWallet output) {
    if (!(output instanceof MoneroOutputWallet)) return false;
    
    // filter on output
    if (this.getAccountIndex() != null && !this.getAccountIndex().equals(output.getAccountIndex())) return false;
    if (this.getSubaddressIndex() != null && !this.getSubaddressIndex().equals(output.getSubaddressIndex())) return false;
    if (this.getAmount() != null && this.getAmount().compareTo(output.getAmount()) != 0) return false;
    if (this.isSpent() != null && !this.isSpent().equals(output.isSpent())) return false;
    
    // filter on output key image
    if (this.getKeyImage() != null) {
      if (output.getKeyImage() == null) return false;
      if (this.getKeyImage().getHex() != null && !this.getKeyImage().getHex().equals(output.getKeyImage().getHex())) return false;
      if (this.getKeyImage().getSignature() != null && !this.getKeyImage().getSignature().equals(output.getKeyImage().getSignature())) return false;
    }
    
    // filter on extensions
    if (this.getSubaddressIndices() != null && !this.getSubaddressIndices().contains(output.getSubaddressIndex())) return false;
    
    // filter with tx query
    if (this.getTxQuery() != null && !this.getTxQuery().meetsCriteria(output.getTx())) return false;
    
    // filter on remaining fields
    if (this.getMinAmount() != null && (this.getAmount() == null || this.getAmount().compareTo(this.getMinAmount()) < 0)) return false;
    if (this.getMaxAmount() != null && (this.getAmount() == null || this.getAmount().compareTo(this.getMaxAmount()) > 0)) return false;
    
    // output meets query
    return true;
  }
  
  /**
   * Indicates if this output query is default, specifying no query options.
   * 
   * @return true if the query is default, false otherwise
   */
  @JsonIgnore
  public boolean isDefault() {
    return meetsCriteria(EMPTY_OUTPUT);
  }
  
  // ------------------- OVERRIDE CO-VARIANT RETURN TYPES ---------------------

  @Override
  public MoneroOutputQuery setTx(MoneroTx tx) {
    super.setTx(tx);
    return this;
  }

  @Override
  public MoneroOutputQuery setTx(MoneroTxWallet tx) {
    super.setTx(tx);
    return this;
  }

  @Override
  public MoneroOutputQuery setAccountIndex(Integer accountIndex) {
    super.setAccountIndex(accountIndex);
    return this;
  }

  @Override
  public MoneroOutputQuery setSubaddressIndex(Integer subaddressIndex) {
    super.setSubaddressIndex(subaddressIndex);
    return this;
  }

  @Override
  public MoneroOutputQuery setIsSpent(Boolean isSpent) {
    super.setIsSpent(isSpent);
    return this;
  }
  
  @Override
  public MoneroOutputQuery setKeyImage(MoneroKeyImage keyImage) {
    super.setKeyImage(keyImage);
    return this;
  }

  @Override
  public MoneroOutputQuery setAmount(BigInteger amount) {
    super.setAmount(amount);
    return this;
  }

  @Override
  public MoneroOutputQuery setIndex(Integer index) {
    super.setIndex(index);
    return this;
  }

  @Override
  public MoneroOutputQuery setRingOutputIndices(List<Integer> ringOutputIndices) {
    super.setRingOutputIndices(ringOutputIndices);
    return this;
  }

  @Override
  public MoneroOutputQuery setStealthPublicKey(String stealthPublicKey) {
    super.setStealthPublicKey(stealthPublicKey);
    return this;
  }
}
