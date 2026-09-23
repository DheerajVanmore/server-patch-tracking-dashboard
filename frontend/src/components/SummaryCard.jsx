function SummaryCard({ title, value, color }) {
  return (
    <div className="summary-card" style={{ backgroundColor: color }}>
      <h3>{title}</h3>
      <div className="value">{value}</div>
    </div>
  );
}

export default SummaryCard;
