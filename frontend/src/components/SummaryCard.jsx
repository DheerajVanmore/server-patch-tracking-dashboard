function SummaryCard({ title, value, color }) {
  return (
    <div className="summary-card" style={{ borderLeft: `4px solid ${color}` }}>
      <h3 style={{ color: 'var(--text-secondary)' }}>{title}</h3>
      <div className="value" style={{ color: 'var(--text-primary)' }}>{value}</div>
    </div>
  );
}

export default SummaryCard;
