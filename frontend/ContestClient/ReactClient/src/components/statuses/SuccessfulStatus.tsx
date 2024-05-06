
type SuccessfulStatusPropTypes = {
    message: string,
}

const SuccessfulStatus = ({message}: SuccessfulStatusPropTypes) => {
    return (
        <div className={'successful_status'}>
            {message}
        </div>
    );
};

export default SuccessfulStatus;