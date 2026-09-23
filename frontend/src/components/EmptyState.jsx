function EmptyState({ message = "No data found." }) {
  return (
    <div style={{ 
      padding: '40px', 
      textAlign: 'center', 
      color: 'var(--text-secondary)',
      backgroundColor: 'var(--surface-color)',
      borderRadius: '8px',
      boxShadow: '0 1px 3px rgba(0,0,0,0.1)'
    }}>
      {message}
    </div>
  );
}

export default EmptyState;
