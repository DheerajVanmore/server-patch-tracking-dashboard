function ErrorMessage({ message }) {
  return (
    <div style={{ 
      padding: '15px', 
      backgroundColor: '#fce8e6', 
      color: '#c5221f', 
      borderRadius: '4px',
      marginBottom: '20px'
    }}>
      <strong>Error:</strong> {message}
    </div>
  );
}

export default ErrorMessage;
